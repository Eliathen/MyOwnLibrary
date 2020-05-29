
import com.google.gson.annotations.SerializedName
import com.szymanski.myownlibrary.data.openLibraryAPI.models.Book

data class BookInfo (
    @SerializedName("details")
    val book: Book
)