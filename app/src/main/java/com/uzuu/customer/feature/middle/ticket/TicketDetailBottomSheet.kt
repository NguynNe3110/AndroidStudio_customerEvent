package com.uzuu.customer.feature.middle.ticket

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.uzuu.customer.databinding.BottomsheetTicketDetailBinding
import com.uzuu.customer.domain.model.MyTicket

class TicketDetailBottomSheet(
    private val ticket: MyTicket
) : BottomSheetDialogFragment() {

    private var _binding: BottomsheetTicketDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomsheetTicketDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindTicketInfo()
        displayQrCode()
    }

    private fun bindTicketInfo() {
        binding.tvEventName.text      = ticket.eventName
        binding.tvTicketType.text     = ticket.ticketTypeName
        binding.tvTicketCode.text     = "Mã vé: ${ticket.ticketCode}"
        binding.tvUsedAt.text         = if (ticket.usedAt != null) "Dùng lúc: ${ticket.usedAt}" else ""
        binding.tvUsedAt.visibility   = if (ticket.usedAt != null) View.VISIBLE else View.GONE

        val (label, colorRes) = when (ticket.status) {
            "VALID"   -> "● Hợp lệ"  to com.uzuu.customer.R.color.event_completed
            "USED"    -> "● Đã dùng" to com.uzuu.customer.R.color.blue_text_secondary
            "EXPIRED" -> "● Hết hạn" to com.uzuu.customer.R.color.event_ongoing
            else      -> "● ${ticket.status}" to com.uzuu.customer.R.color.blue_text_secondary
        }
        binding.tvStatus.text = label
        binding.tvStatus.setTextColor(requireContext().getColor(colorRes))
    }

    /**
     * qrCode từ server là chuỗi base64 của ảnh QR.
     * Decode ra Bitmap rồi set vào ImageView.
     * Nếu server trả về URL thay vì base64 → dùng Glide làm fallback.
     */
    private fun displayQrCode() {
        val qrString = ticket.qrCode
        if (qrString.isBlank()) {
            binding.imgQr.visibility    = View.GONE
            binding.tvQrHint.visibility = View.GONE
            return
        }

        try {
            val cleanBase64 = qrString
                .removePrefix("data:image/png;base64,")
                .removePrefix("data:image/jpeg;base64,")
            val bytes = Base64.decode(cleanBase64, Base64.DEFAULT)
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            binding.imgQr.setImageBitmap(bitmap)
        } catch (e: Exception) {
            // Fallback: nếu không phải base64 (ví dụ server trả URL)
            try {
                com.bumptech.glide.Glide.with(this)
                    .load(qrString)
                    .into(binding.imgQr)
            } catch (_: Exception) {
                binding.imgQr.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}