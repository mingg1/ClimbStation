package fi.metropolia.climbstation

import android.content.Context
import android.util.Log
import java.io.*


/**
 * Loader to load IniFile
 */
open class IniFileLoader(private val context: Context) {
    // HashMap<section, HashMap<key, String>>To
    private var mDataMap: HashMap<String?, HashMap<String, String>>? = null

    //File Path
    private var mFilePath: String? = null

    //Is load completed?
    private var mIsLoaded = false

    /**
     * Load the specified file
     *
     * @param filePath File path
     * @return Whether the load was successful
     */
    fun load(filePath: String?): Boolean {
        mFilePath = filePath
        return loadProcess(filePath)
    }

    /**
     * Reload the file you last tried to load
     *
     * @return Whether the load was successful
     */
    fun reload(): Boolean {
        return isEmpty(mFilePath) && loadProcess(mFilePath)
    }

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
            Log.e("err",e.toString())
            return false
        }
        mIsLoaded = true
        return true
    }

    /**
     * Read the result(section, (key, value))of[HashMap]Return with
     *
     * @return The result of reading[HashMap]
     */
    val allDataMap: HashMap<String?, HashMap<String, String>>?
        get() = if (mIsLoaded) {
            mDataMap
        } else null

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
     * Returns the value of the specified section, specified key
     *
     * @param section Section specification
     * @param key key specification
     * @return Specified section, specified key value
     */
    fun getValue(section: String?, key: String): String? {
        if (mIsLoaded) {
            val map = mDataMap!![section]
            if (map != null) {
                return map[key]
            }
        }
        return null
    }

    /**
     * Returns whether the specified section is in the read result
     *
     * @param section Section specification
     * @return if it exists`true`
     */
    fun containsSection(section: String?): Boolean {
        return if (mIsLoaded) {
            mDataMap!!.containsKey(section)
        } else false
    }

    /**
     * Returns whether the specified key in the specified section is in the read result
     *
     * @param section Section specification
     * @param key key specification
     * @return if it exists`true`
     */
    fun containsKey(section: String?, key: String): Boolean {
        if (mIsLoaded) {
            val map: HashMap<String, String>? = mDataMap!![section]
            return map != null && map.containsKey(key)
        }
        return false
    }

    /**
     * `String`Determine if is empty
     *
     * @param str Judgment target
     * @return `String`If is empty`true`
     */
    private fun isEmpty(str: String?): Boolean {
        return str == null || str.isEmpty()
    }
}