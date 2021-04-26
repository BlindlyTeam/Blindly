package ch.epfl.sdp.blindly

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import ch.epfl.sdp.blindly.user.UserRepository
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito.mock
import javax.inject.Inject

class ProfilePageViewModelUnitTest
//TODO missing tests for the PageViewModel