import com.mycompany.chservicetime.data.source.local.TimeslotEntity

class InitData {
    companion object {
        val timeslotEntity_1 = TimeslotEntity(
            "Timeslot Entity 1",
            "Work Time",
            9,
            0,
            5,
            0,
            false,
            true,
            true,
            true,
            true,
            true,
            false
        )

        val timeslotEntity_2 = TimeslotEntity(
            "Timeslot Entity 2",
            "School First Phrase",
            8,
            30,
            10,
            0,
            false,
            true,
            true,
            false,
            true,
            false,
            false
        )
    }
}