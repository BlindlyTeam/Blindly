@file:JvmName("Uri")

package android.net
import org.mockito.kotlin.mock
import java.io.File

// Mocks for the android Uri function in tests
class Uri {
    companion object {
        @JvmStatic
        fun fromFile(file: File): Uri = mock<Uri>()
    }
}