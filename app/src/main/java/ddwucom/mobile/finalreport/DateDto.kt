package ddwucom.mobile.finalreport

class DateDto(var p1: Int, var p2: Int, var p3: Int) : java.io.Serializable {
    override fun toString(): String {
        return "($p1 년 $p2 월 $p3 )에 개발자 정보를 확인하였습니다"
    }
}