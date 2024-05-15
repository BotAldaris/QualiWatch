package com.example.qualiwatch.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.example.qualiwatch.data.source.local.OfflineProductDataSource
import com.example.qualiwatch.data.source.local.QualiwatchDatabase
import com.example.qualiwatch.data.source.network.NetworkProductsRepository
import com.example.qualiwatch.model.ImageResponse
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
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
    )

    fun isNetworkAvailable(): Boolean
    val nearExpirationRepository: NearExpirationRepository
}

class DefaultAppContainer(context: Context) : AppContainer {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val baseUrl = "http://10.0.2.2:5000"
    private val client = HttpClient(CIO){
        install(ContentNegotiation){
            json()
        }
    }
    private val pat: Pattern = Pattern.compile("\\d{1,2}[-./\\s]?\\d{1,2}[-./\\s]?\\d{2,4}")
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val net = NetworkProductsRepository(client,baseUrl)
    private val off = OfflineProductDataSource(QualiwatchDatabase.getDatabase(context).productDao())
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

    override fun isNetworkAvailable(): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }

    override val nearExpirationRepository: NearExpirationRepository =
        NearExpirationRepository(net)


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