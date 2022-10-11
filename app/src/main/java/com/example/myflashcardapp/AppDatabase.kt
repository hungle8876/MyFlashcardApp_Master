package com.example.myflashcardapp

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myflashcardapp.Flashcard
import com.example.myflashcardapp.FlashcardDao

@Database(entities = [Flashcard::class], version = 1)

abstract class AppDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao
}
