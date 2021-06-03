package ch.epfl.sdp.blindly.audio

import android.net.Uri
import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import java.io.File
import java.util.concurrent.CompletableFuture


private const val NAME_AUDIO_ONE = "Audio 1"
private const val NAME_AUDIO_TWO = "Audio 2"
private const val DURATION_ZERO = "00:00"
private const val DURATION_ONE = "00:01"
private const val FILEPATH1 = "Test/myApp/file1"
private const val FILEPATH2 = "Test/myApp/file2"

class FirebaseRecordingsTest {
    private fun getFirebaseRecordings(
        uploadTask: UploadTask,
        downloadTask: FileDownloadTask,
        deleteTask: Task<Void>
    ): FirebaseRecordings {
        val childStorageRef = mock(StorageReference::class.java)

        `when`(childStorageRef.putFile(any())).thenReturn(uploadTask)
        `when`(childStorageRef.getFile(any<File>())).thenReturn(downloadTask)
        `when`(childStorageRef.delete()).thenReturn(deleteTask)

        val storageRef = mock(StorageReference::class.java)
        `when`(storageRef.child(any())).thenReturn(childStorageRef)

        val storage = mock(FirebaseStorage::class.java)
        `when`(storage.reference).thenReturn(storageRef)

        return FirebaseRecordings(storage)
    }


    private class MockedDeleteTask: MockedTask<Task<Void>> {
        override val task: Task<Void> = mock();

        constructor() {
            `when`(task.addOnCanceledListener(any())).thenAnswer {
                val listener = it.getArgument<OnCanceledListener>(0)
                canceledListener.add(listener)
                task
            }
            `when`(task.addOnCompleteListener(any())).thenAnswer {
                val listener = it.getArgument<OnCompleteListener<Task<Void>>>(0)
                completedListener.add(listener)
                task
            }
            `when`(task.addOnFailureListener(any())).thenAnswer {
                val listener = it.getArgument<OnFailureListener>(0)
                failedListener.add(listener)
                task
            }
        }
    }
    private class MockedDownloadTask: MockedTask<FileDownloadTask> {
        override val task: FileDownloadTask = mock();

        constructor() {
            `when`(task.addOnCanceledListener(any())).thenAnswer {
                val listener = it.getArgument<OnCanceledListener>(0)
                canceledListener.add(listener)
                task
            }
            `when`(task.addOnCompleteListener(any())).thenAnswer {
                val listener = it.getArgument<OnCompleteListener<FileDownloadTask>>(0)
                completedListener.add(listener)
                task
            }
            `when`(task.addOnFailureListener(any())).thenAnswer {
                val listener = it.getArgument<OnFailureListener>(0)
                failedListener.add(listener)
                task
            }
        }
    }

    private class MockedUploadTask: MockedTask<UploadTask> {
        override val task: UploadTask = mock();

        constructor() {
            `when`(task.addOnCanceledListener(any())).thenAnswer {
                val listener = it.getArgument<OnCanceledListener>(0)
                canceledListener.add(listener)
                task
            }
            `when`(task.addOnCompleteListener(any())).thenAnswer {
                val listener = it.getArgument<OnCompleteListener<UploadTask>>(0)
                completedListener.add(listener)
                task
            }
            `when`(task.addOnFailureListener(any())).thenAnswer {
                val listener = it.getArgument<OnFailureListener>(0)
                failedListener.add(listener)
                task
            }
        }
    }
    private abstract class MockedTask<T> {
        protected val canceledListener = mutableListOf<OnCanceledListener>()
        protected val completedListener = mutableListOf<OnCompleteListener<T>>()
        protected val failedListener = mutableListOf<OnFailureListener>()
        abstract val task: T


        fun cancel() {
            canceledListener.forEach { it.onCanceled() }
        }

        fun complete(success: Boolean) {
            val taskSanpshot = mock(Task::class.java)
            `when`(taskSanpshot.isSuccessful).thenReturn(success)
            completedListener.forEach { it.onComplete(taskSanpshot as Task<T>) }
        }

        fun fail(e: Exception = Exception()) {
            failedListener.forEach { it.onFailure(e) }
        }
    }

    @get:Rule
    var folder = TemporaryFolder()

    private fun getCallback(future: CompletableFuture<Boolean>): Recordings.RecordingOperationCallback {
        return object : Recordings.RecordingOperationCallback() {
            override fun onSuccess() {
                future.complete(true)
            }

            override fun onError() {
                future.complete(false)
            }

        }
    }

    private lateinit var file1: File;
    private val recordingPath = "test"

    @Before
    fun setup() {
        file1 = folder.newFile("testfile1.txt")
    }

    private fun callPutFile(
        future: CompletableFuture<Boolean>,
        uploadTask: MockedUploadTask
        ) {
        getFirebaseRecordings(uploadTask.task, MockedDownloadTask().task, MockedDeleteTask().task).putFile(recordingPath, file1, getCallback(future))
    }
    private fun callGetFile(
        future: CompletableFuture<Boolean>,
        downloadTask: MockedDownloadTask
    ) {
        getFirebaseRecordings(MockedUploadTask().task, downloadTask.task, MockedDeleteTask().task).getFile(recordingPath, file1, getCallback(future))
    }
    private fun callDeleteFile(
        future: CompletableFuture<Boolean>,
        deleteTask: MockedDeleteTask
    ) {
        getFirebaseRecordings(MockedUploadTask().task, MockedDownloadTask().task, deleteTask.task).deleteFile(recordingPath, getCallback(future))
    }


    @Test
    fun putFileCompleteSucessfulCallbackIsCalled() {
        val task = MockedUploadTask()
        val future = CompletableFuture<Boolean>();
        callPutFile(future, task)
        task.complete(true)
        assertThat(future.get(), equalTo(true))
    }

    @Test
    fun putFileCompleteUnsucessfulCallbackIsCalled() {
        val task = MockedUploadTask()
        val future = CompletableFuture<Boolean>();
        callPutFile(future, task)
        task.complete(false)
        assertThat(future.get(), equalTo(false))
    }

    @Test
    fun putFileFailureCallbackIsCalled() {
        val task = MockedUploadTask()
        val future = CompletableFuture<Boolean>();
        callPutFile(future, task)
        task.fail()
        assertThat(future.get(), equalTo(false))
    }

    @Test
    fun putFileCancelledCallbackIsCalled() {
        val task = MockedUploadTask()
        val future = CompletableFuture<Boolean>();
        callPutFile(future, task)
        task.cancel()
        assertThat(future.get(), equalTo(false))
    }

    @Test
    fun getFileCompleteSucessfulCallbackIsCalled() {
        val task = MockedDownloadTask()
        val future = CompletableFuture<Boolean>();
        callGetFile(future, task)
        task.complete(true)
        assertThat(future.get(), equalTo(true))
    }

    @Test
    fun getFileCompleteUnsucessfulCallbackIsCalled() {
        val task = MockedDownloadTask()
        val future = CompletableFuture<Boolean>();
        callGetFile(future, task)
        task.complete(false)
        assertThat(future.get(), equalTo(false))
    }

    @Test
    fun getFileFailureCallbackIsCalled() {
        val task = MockedDownloadTask()
        val future = CompletableFuture<Boolean>();
        callGetFile(future, task)
        task.fail()
        assertThat(future.get(), equalTo(false))
    }

    @Test
    fun getFileCancelledCallbackIsCalled() {
        val task = MockedDownloadTask()
        val future = CompletableFuture<Boolean>();
        callGetFile(future, task)
        task.cancel()
        assertThat(future.get(), equalTo(false))
    }

    // Delete
    @Test
    fun deleteFileCompleteSucessfulCallbackIsCalled() {
        val task = MockedDeleteTask()
        val future = CompletableFuture<Boolean>();
        callDeleteFile(future, task)
        task.complete(true)
        assertThat(future.get(), equalTo(true))
    }

    @Test
    fun deleteFileCompleteUnsucessfulCallbackIsCalled() {
        val task = MockedDeleteTask()
        val future = CompletableFuture<Boolean>();
        callDeleteFile(future, task)
        task.complete(false)
        assertThat(future.get(), equalTo(false))
    }

    @Test
    fun deleteFileFailureCallbackIsCalled() {
        val task = MockedDeleteTask()
        val future = CompletableFuture<Boolean>();
        callDeleteFile(future, task)
        task.fail()
        assertThat(future.get(), equalTo(false))
    }

    @Test
    fun deleteFileCancelledCallbackIsCalled() {
        val task = MockedDeleteTask()
        val future = CompletableFuture<Boolean>();
        callDeleteFile(future, task)
        task.cancel()
        assertThat(future.get(), equalTo(false))
    }
}