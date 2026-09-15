package com.example.smartdevicegenerator.data

import com.example.smartdevicegenerator.model.AndroidVersion

object AndroidVersionDataset {
    val versions: List<AndroidVersion> = listOf(
        AndroidVersion("Android 1.0", 1, "Base", "BASE", "September 2008", true, 2008..2009),
        AndroidVersion("Android 1.1", 2, "Base 1.1", "BASE_1_1", "February 2009", true, 2009..2009),
        AndroidVersion("Android 1.5", 3, "Cupcake", "CUPCAKE", "April 2009", true, 2009..2010),
        AndroidVersion("Android 1.6", 4, "Donut", "DONUT", "September 2009", true, 2009..2010),
        AndroidVersion("Android 2.0", 5, "Eclair", "ECLAIR", "October 2009", true, 2009..2010),
        AndroidVersion("Android 2.0.1", 6, "Eclair", "ECLAIR_0_1", "December 2009", true, 2009..2010),
        AndroidVersion("Android 2.1", 7, "Eclair", "ECLAIR_MR1", "January 2010", true, 2010..2011),
        AndroidVersion("Android 2.2", 8, "Froyo", "FROYO", "May 2010", true, 2010..2012),
        AndroidVersion("Android 2.3", 9, "Gingerbread", "GINGERBREAD", "December 2010", true, 2010..2012),
        AndroidVersion("Android 2.3.3", 10, "Gingerbread", "GINGERBREAD_MR1", "February 2011", true, 2011..2013),
        AndroidVersion("Android 3.0", 11, "Honeycomb", "HONEYCOMB", "February 2011", true, 2011..2012),
        AndroidVersion("Android 3.1", 12, "Honeycomb", "HONEYCOMB_MR1", "May 2011", true, 2011..2012),
        AndroidVersion("Android 3.2", 13, "Honeycomb", "HONEYCOMB_MR2", "July 2011", true, 2011..2013),
        AndroidVersion("Android 4.0", 14, "Ice Cream Sandwich", "ICE_CREAM_SANDWICH", "October 2011", true, 2011..2013),
        AndroidVersion("Android 4.0.3", 15, "Ice Cream Sandwich", "ICE_CREAM_SANDWICH_MR1", "December 2011", true, 2011..2013),
        AndroidVersion("Android 4.1", 16, "Jelly Bean", "JELLY_BEAN", "July 2012", true, 2012..2014),
        AndroidVersion("Android 4.2", 17, "Jelly Bean", "JELLY_BEAN_MR1", "November 2012", true, 2012..2014),
        AndroidVersion("Android 4.3", 18, "Jelly Bean", "JELLY_BEAN_MR2", "July 2013", true, 2013..2015),
        AndroidVersion("Android 4.4", 19, "KitKat", "KITKAT", "October 2013", true, 2013..2016),
        AndroidVersion("Android 4.4W", 20, "KitKat Wear", "KITKAT_WATCH", "June 2014", true, 2014..2015),
        AndroidVersion("Android 5.0", 21, "Lollipop", "LOLLIPOP", "November 2014", false, 2014..2016),
        AndroidVersion("Android 5.1", 22, "Lollipop", "LOLLIPOP_MR1", "March 2015", false, 2015..2017),
        AndroidVersion("Android 6.0", 23, "Marshmallow", "M", "October 2015", false, 2015..2018),
        AndroidVersion("Android 7.0", 24, "Nougat", "N", "August 2016", false, 2016..2018),
        AndroidVersion("Android 7.1", 25, "Nougat", "N_MR1", "October 2016", false, 2016..2019),
        AndroidVersion("Android 8.0", 26, "Oreo", "O", "August 2017", false, 2017..2020),
        AndroidVersion("Android 8.1", 27, "Oreo", "O_MR1", "December 2017", false, 2017..2020),
        AndroidVersion("Android 9", 28, "Pie", "P", "August 2018", false, 2018..2021),
        AndroidVersion("Android 10", 29, "Android 10", "Q", "September 2019", false, 2019..2022),
        AndroidVersion("Android 11", 30, "Android 11", "R", "September 2020", false, 2020..2023),
        AndroidVersion("Android 12", 31, "Android 12", "S", "October 2021", false, 2021..2024),
        AndroidVersion("Android 12L", 32, "Android 12L", "S_V2", "March 2022", false, 2022..2024),
        AndroidVersion("Android 13", 33, "Tiramisu", "TIRAMISU", "August 2022", false, 2022..2025),
        AndroidVersion("Android 14", 34, "Upside Down Cake", "UPSIDE_DOWN_CAKE", "October 2023", false, 2023..2026),
        AndroidVersion("Android 15", 35, "Vanilla Ice Cream", "VANILLA_ICE_CREAM", "September 2024", false, 2024..2027),
        AndroidVersion("Android 16", 36, "Baklava", "BAKLAVA", "June 2025", false, 2025..2028),
        AndroidVersion("Android 17", 37, "Cinnamon Roll", "CINNAMON_ROLL", "June 2026", false, 2026..2031)
    )

    private val versionMap: Map<Int, AndroidVersion> = versions.associateBy { it.apiLevel }

    fun getByApiLevel(apiLevel: Int): AndroidVersion {
        return versionMap[apiLevel] ?: versions.last()
    }

    fun getAvailableVersions(includeLegacy: Boolean): List<AndroidVersion> {
        return if (includeLegacy) versions else versions.filter { !it.isLegacy }
    }
}
