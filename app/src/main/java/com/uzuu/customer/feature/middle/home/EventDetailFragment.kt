package com.uzuu.customer.feature.middle.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.uzuu.customer.databinding.FragmentEventDetailBinding
import com.uzuu.customer.domain.model.Event
import com.uzuu.customer.ui.adapter.CategoryTicketAdapter
import com.uzuu.customer.ui.adapter.EventAdapter

class EventDetailFragment : Fragment() {

    private var _binding: FragmentEventDetailBinding? = null
    val binding get() = _binding!!

    private val args: EventDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val event = args.event
        bindData(event)
    }

    private fun bindData(event: Event) {
        // Ảnh banner
        val imageUrl = EventAdapter.fixImageUrl(event.imageUrls.firstOrNull())
        Glide.with(binding.imgEventDetail)
            .load(imageUrl)
            .centerCrop()
            .into(binding.imgEventDetail)

        binding.txtEventName.text       = event.name
        binding.txtCategory.text        = event.categoryName
        binding.txtLocation.text        = event.location
        binding.txtStartTime.text       = "Bắt đầu: ${event.startTime ?: "Chưa xác định"}"
        binding.txtEndTime.text         = "Kết thúc: ${event.endTime ?: "Chưa xác định"}"
        binding.txtSaleStart.text       = "Mở bán: ${event.saleStartDate ?: "Chưa xác định"}"
        binding.txtSaleEnd.text         = "Kết thúc bán: ${event.saleEndDate ?: "Chưa xác định"}"
        binding.txtDescription.text     = event.description?.ifBlank { "Chưa có mô tả." } ?: "Chưa có mô tả."

        // Trạng thái + màu
        val (statusLabel, statusColor) = when (event.status) {
            "PENDING"    -> "● Sắp diễn ra"  to requireContext().getColor(com.uzuu.customer.R.color.event_upcoming)
            "ON_SALE"    -> "● Đang bán vé"  to requireContext().getColor(com.uzuu.customer.R.color.event_on_sale)
            "ONGOING"    -> "● Đang diễn ra" to requireContext().getColor(com.uzuu.customer.R.color.event_ongoing)
            "COMPLETED"  -> "● Đã kết thúc"  to requireContext().getColor(com.uzuu.customer.R.color.event_completed)
            else         -> "● ${event.status}" to requireContext().getColor(com.uzuu.customer.R.color.blue_text_secondary)
        }
        binding.txtStatus.text      = statusLabel
        binding.txtStatus.setTextColor(statusColor)

        // RecyclerView loại vé (dùng lại adapter, tắt click +/-)
        val ticketAdapter = CategoryTicketAdapter()
        binding.recyclerTicketTypes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ticketAdapter
            setHasFixedSize(false)
        }
        ticketAdapter.submitList(event.ticketTypes)

        // Nút mua ngay → mở bottom sheet
        binding.btnBuyNow.setOnClickListener {
            // Quay lại và mở bottom sheet từ HomeFragment
            // Cách đơn giản: pop back thì HomeFragment sẽ tự mở lại bottom sheet
            // Ở đây ta dùng cách truyền result về HomeFragment
            val result = Bundle().apply { putParcelable("open_bottom_sheet", event) }
            parentFragmentManager.setFragmentResult("event_detail_result", result)
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}