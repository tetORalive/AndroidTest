package xyz.tetatet.vivaquiz.extensions

import android.view.View
import androidx.core.view.ViewCompat
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject

fun View.rotate(rotation: Float, duration: Long = 1000L): Completable {
    val animationSubject = CompletableSubject.create()
    return animationSubject.doOnSubscribe {
        ViewCompat.animate(this)
            .setDuration(duration)
            .rotation(rotation)
            .withEndAction {
                animationSubject.onComplete()
            }
    }
}