package xyz.tetatet.vivaquiz.ui.main.adapter

import android.view.View
import android.view.ViewGroup
import com.google.android.libraries.places.api.model.AutocompletePrediction
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.list_item_google_address_prediction.view.*
import xyz.tetatet.vivaquiz.R
import xyz.tetatet.vivaquiz.ui.base.BaseAdapter


class GooglePredictionsAdapter : BaseAdapter<AutocompletePrediction?>() {

    /*Constants --------------------------------------------------------------------------*/
    companion object;

    /*Fields -----------------------------------------------------------------------------*/
    val placeSelection: PublishSubject<Pair<String, String>> = PublishSubject.create()

    /*Inner Functions --------------------------------------------------------------------*/
    override fun createItemViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
            PredictionsViewHolder(inflate(R.layout.list_item_google_address_prediction, parent))

    inner class PredictionsViewHolder(itemView: View) : BaseViewHolder(itemView) {

        override fun onBindData(item: AutocompletePrediction?) {

            itemView.apply {
                val name = item?.getFullText(null).toString()
                googlePrediction.apply {
                    text = name
                }
                setOnClickListener {
                    item?.let { placeSelection.onNext(Pair(it.placeId, name)) }
                }
            }
        }
    }
}