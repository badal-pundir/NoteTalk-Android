package com.example.notetalk.di

import android.app.Application
import androidx.room.Room
import com.example.notetalk.data.INoteRepository
import com.example.notetalk.data.NoteDao
import com.example.notetalk.data.NoteDatabase
import com.example.notetalk.data.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            "note_database"
        )
            .addMigrations(NoteDatabase.MIGRATION_1_2)
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(database: NoteDatabase): NoteDao = database.noteDao()

    @Provides
    @Singleton
    fun provideNoteRepository(dao: NoteDao): INoteRepository = NoteRepository(dao)
}