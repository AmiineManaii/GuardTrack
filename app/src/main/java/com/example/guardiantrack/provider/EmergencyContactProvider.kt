package com.example.guardiantrack.provider

import android.content.*
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.example.guardiantrack.data.local.db.GuardianDatabase
import dagger.hilt.android.EntryPointAccessors
import com.example.guardiantrack.di.ContentProviderEntryPoint
import kotlinx.coroutines.runBlocking

class EmergencyContactProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.example.guardiantrack.provider"
        const val PATH_CONTACTS = "emergency_contacts"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH_CONTACTS")

        const val COL_ID = "_id"
        const val COL_NAME = "name"
        const val COL_PHONE = "phone_number"

        private val URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, PATH_CONTACTS, 1)
            addURI(AUTHORITY, "$PATH_CONTACTS/#", 2)
        }
    }

    private lateinit var database: GuardianDatabase

    override fun onCreate(): Boolean {
        val entryPoint = EntryPointAccessors.fromApplication(
            context!!.applicationContext,
            ContentProviderEntryPoint::class.java
        )
        database = entryPoint.guardianDatabase()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?,
        selection: String?, selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {
        val contacts = runBlocking { database.contactDao().getAllContactsSync() }

        val columns = arrayOf(COL_ID, COL_NAME, COL_PHONE)
        val cursor = MatrixCursor(columns)

        when (URI_MATCHER.match(uri)) {
            1 -> contacts.forEach { c ->
                cursor.addRow(arrayOf(c.id, c.name, c.phoneNumber))
            }
            2 -> {
                val id = ContentUris.parseId(uri)
                contacts.find { it.id == id }?.let { c ->
                    cursor.addRow(arrayOf(c.id, c.name, c.phoneNumber))
                }
            }
        }

        cursor.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String = when (URI_MATCHER.match(uri)) {
        1 -> "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_CONTACTS"
        2 -> "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_CONTACTS"
        else -> throw IllegalArgumentException("Unknown URI: $uri")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?) = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?) = 0
}