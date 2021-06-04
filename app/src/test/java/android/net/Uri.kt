@file:JvmName("Uri")

package android.net
import org.mockito.kotlin.mock
import java.io.File

// Mocks for the android Uri function in tests
// Suppress warning as the lint doesn't see it is used in test
@Suppress("unused")
class Uri {
    companion object {
        @JvmStatic
        // Suppress unused as we need to match the signature of the original
        @Suppress("unused", "UNUSED_PARAMETER")
        fun fromFile(file: File): Uri = mock()
    }
}