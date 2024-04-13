package com.example.qualiwatch.data

import android.content.Context
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.example.qualiwatch.data.source.local.NearExpirationDataSource
import com.example.qualiwatch.data.source.local.OfflineProductDataSource
import com.example.qualiwatch.data.source.local.QualiwatchDatabase
import com.example.qualiwatch.data.source.network.NetworkProductsRepository
import com.example.qualiwatch.model.ImageResponse
import com.example.qualiwatch.network.ProductsApiService
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.regex.Matcher
import java.util.regex.Pattern

interface AppContainer {
    val productsRepository: ProductsRepository
    var online: Boolean
    val dateMatcher: Matcher
    val userPreferencesRepository: UserPreferencesRepository
    fun imageToText(
        imageProxy: ImageProxy,
        num: Int,
        updateList: (List<ImageResponse>) -> Unit
    ): Unit

    val nearExpirationRepository: NearExpirationRepository
}

class DefaultAppContainer(context: Context) : AppContainer {
    private val baseUrl = "http://cefsaweb/QualiwatchAppCozinha/"
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()
    private val retrofitService: ProductsApiService by lazy {
        retrofit.create(ProductsApiService::class.java)
    }
    private val pat: Pattern = Pattern.compile("\\d{1,2}[-./\\s]?\\d{1,2}[-./\\s]?\\d{2,4}")
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val net = NetworkProductsRepository(retrofitService)
    private val off = OfflineProductDataSource(QualiwatchDatabase.getDatabase(context).productDao())
    private val nearExpirationDataSource =
        NearExpirationDataSource(QualiwatchDatabase.getDatabase(context).expirationDao())
    override val dateMatcher: Matcher = pat.matcher("")
    override val userPreferencesRepository: UserPreferencesRepository =
        UserPreferencesRepository(QualiwatchDatabase.getDatabase(context).userPreferencesDao())


    @OptIn(ExperimentalGetImage::class)
    override fun imageToText(
        imageProxy: ImageProxy,
        num: Int,
        updateList: (List<ImageResponse>) -> Unit
    ) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            recognizer.process(image).addOnSuccessListener { visionText ->
                updateList(textToImageResponseList(visionText, num))
            }
        }
    }

    override val nearExpirationRepository: NearExpirationRepository =
        NearExpirationRepository(nearExpirationDataSource, net, userPreferencesRepository)


    override var online: Boolean = false
    override val productsRepository: ProductsRepository by lazy {
        OnOffProductsRepository(
            offlineProductRepository = off,
            networkProductsRepository = net,
            preferencesRepository = userPreferencesRepository
        )
    }

    private fun textToImageResponseList(text: Text, type: Int): List<ImageResponse> {
        val imageResponseListMlkit = mutableListOf<ImageResponse>()
        var ids = 1
        for (block in text.textBlocks) {
            for (line in block.lines) {
                if (type == 3) {
                    if (dateMatcher.reset(line.text).find()) {
                        imageResponseListMlkit.add(ImageResponse(ids, dateMatcher.group()))
                        ids++
                    }
                } else {
                    imageResponseListMlkit.add(ImageResponse(ids, line.text))
                    ids++
                }
            }
        }
        return imageResponseListMlkit
    }
}