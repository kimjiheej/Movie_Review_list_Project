package ddwucom.mobile.finalreport


data class MovieDto (val id : Long, var photo : Int ,var director : String, var movieTitle : String, var rate : String, var genre : String) : java.io.Serializable{

    override fun toString() = ""
}