package fi.metropolia.climbstation

import android.content.Context
import android.util.Log
import java.io.*


/**
 * Loader to load IniFile
 * @author Minji Choi
 */
open class ConfigReader(private val context: Context) {
    // HashMap<section, HashMap<key, String>>
    private var mDataMap: HashMap<String?, HashMap<String, String>>? = null

    //File Path
    private var mFilePath: String? = null

    //Is load completed?
    private var mIsLoaded = false

    /**
     * Load the specified file
     *
     * @param filePath is File path
     * @return Whether the file loadinig was successful
     */
    fun load(filePath: String?): Boolean {
        mFilePath = filePath
        return loadProcess(filePath)
    }

    /**
     * Reload the file you last tried to load
     *
     * @return Whether the file loading was successful
     */
    fun reload(): Boolean {
        return isEmpty(mFilePath) && loadProcess(mFilePath)
    }

    /**
     * read the file loaded and save as hashmap
     * @param filePath is the file path
     * @return Whether the file loading was successful
     */
    private fun loadProcess(filePath: String?): Boolean {
        mIsLoaded = false
        mDataMap = HashMap()
        try {
            val fileReader = context.assets.open(filePath!!)
            val br = fileReader.bufferedReader()
            var line = br.readLine()

            //Section name
            var section: String? = null
            //Key value
            var map = HashMap<String, String>()
            while (line != null) {
                //Remove whitespace at the beginning and end of lines
                line = line.trim()

                //Blank line
                if (isEmpty(line)) {
                    // no process
                } else if (line[0] == '#') {
                    // no process
                } else if (line[0] == '[' && line[line.length - 1] == ']') {
                    section = line.substring(1, line.length - 1)
                    map = HashMap()
                } else if (line.length >= 3 && line.contains("=") && line.length > line.indexOf("=") + 1) {
                    val key = line.substring(0, line.indexOf("="))
                    val value = line.substring(line.indexOf("=") + 1)
                    map[key] = value
                    mDataMap!![section] = map
                }
                line = br.readLine()
            }
            br.close()
        } catch (e: IOException) {
            return false
        }
        mIsLoaded = true
        return true
    }

    /**
     * The specified section of the read result(key, value)of[Map]Return with
     *
     * @param section Section specification
     * @return The specified section of the read result
     */
    fun getSectionDataMap(section: String?): Map<String, String>? {
        return if (mIsLoaded) {
            mDataMap!![section]
        } else null
    }

    /**
     * Check if the string is empty or not
     *
     * @param str Judgment target
     * @return If the string is empty or not
     */
    private fun isEmpty(str: String?): Boolean {
        return str == null || str.isEmpty()
    }
}