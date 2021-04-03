package com.mmfinfotech.streameApp.dashBoard.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mmfinfotech.streameApp.R
import com.mmfinfotech.streameApp.dashBoard.adapter.GiftAdapter
import com.mmfinfotech.streameApp.models.Gift
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.GridSpacingItemDecoration
import com.mmfinfotech.streameApp.utils.RecyclerTouchListener
import kotlinx.android.synthetic.main.partial_layout_gift_bottomsheet.*

/**
 * Created By Somil Rawal on Sat-19-12-2020-December-2020.
 */
class GiftBottomSheet : BottomSheetDialogFragment() {

    private var arrGifts: ArrayList<Gift?>? = null
    private var listners: Listeners? = null
    private var availableCoins: String? = null

    companion object {
        val TAG: String? = GiftBottomSheet::class.java.simpleName
        fun getInstance(arrGifts: ArrayList<Gift?>?, availableCoins: String?) =  GiftBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelableArrayList("Gifts", arrGifts)
                putString("availableCoins", availableCoins)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Listeners) listners = context
        else throw RuntimeException("$context must implement Gift Bottom Listener")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arrGifts = ArrayList()
        val arr = arguments?.getParcelableArrayList<Gift?>(getString(R.string.txt_gift))
        availableCoins = arguments?.getString("availableCoins", AppConstants.Defaults.string)
        arrGifts?.addAll(arr ?: ArrayList())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.partial_layout_gift_bottomsheet, container, false)?.apply {
        findViewById<TextView>(R.id.textViewCoinCount).text = availableCoins
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val space = context?.resources?.getDimensionPixelSize(R.dimen._10sdp)
        recyclerviewGifts.adapter = GiftAdapter(context, arrGifts)
        recyclerviewGifts?.addItemDecoration(GridSpacingItemDecoration(4, space ?: 0, true))

        recyclerviewGifts?.addOnItemTouchListener(RecyclerTouchListener(context, recyclerviewGifts, object : RecyclerTouchListener.ClickListener {
            override fun onClick(view: View, position: Int) {
//                Toast.makeText(context, "${arrGifts?.get(position)}", Toast.LENGTH_SHORT).show()
                listners?.onItemClick(arrGifts?.get(position))
            }

            override fun onLongClick(view: View, position: Int) {

            }
        }))

        buttonBottomSheetGiftBuy?.setOnClickListener {
            dismiss()
            val buyCoinsBottomSheet = BuyCoinsBottomSheet.getInstance()
            buyCoinsBottomSheet.show(fragmentManager!!, BuyCoinsBottomSheet.TAG)
        }
    }

    override fun onDetach() {
        super.onDetach()
        listners = null
    }

    interface Listeners {
        fun onItemClick(gift: Gift?)
    }
}