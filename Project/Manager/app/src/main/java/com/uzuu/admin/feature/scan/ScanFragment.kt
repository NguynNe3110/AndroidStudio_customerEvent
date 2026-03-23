package com.uzuu.admin.feature.scan

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.uzuu.admin.R
import com.uzuu.admin.databinding.FragmentScanBinding
import com.uzuu.admin.feature.MainActivity
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    val binding get() = _binding!!

    private val viewModel: ScanViewModel by viewModels {
        ScanFactory((requireActivity() as MainActivity).container.checkInRepo)
    }

    private lateinit var cameraExecutor: ExecutorService

    // ── Permission launcher ───────────────────────────────────────────────────
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) startCamera()
        else Toast.makeText(context, "Cần cấp quyền Camera để quét QR", Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cameraExecutor = Executors.newSingleThreadExecutor()

        viewModel.init()
        setupButtons()
        observeState()
        observeEvent()
        checkCameraPermission()
    }

    // ── Camera permission ─────────────────────────────────────────────────────
    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> startCamera()

            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // ── CameraX + ML Kit QR ───────────────────────────────────────────────────
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            val scanner = BarcodeScanning.getClient()

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysis ->
                    analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                        val mediaImage = imageProxy.image
                        if (mediaImage != null) {
                            val inputImage = InputImage.fromMediaImage(
                                mediaImage, imageProxy.imageInfo.rotationDegrees
                            )
                            scanner.process(inputImage)
                                .addOnSuccessListener { barcodes ->
                                    for (barcode in barcodes) {
                                        if (barcode.format == Barcode.FORMAT_QR_CODE) {
                                            val rawValue = barcode.rawValue
                                            if (!rawValue.isNullOrBlank()) {
                                                viewModel.onQrScanned(rawValue)
                                            }
                                        }
                                    }
                                }
                                .addOnCompleteListener { imageProxy.close() }
                        } else {
                            imageProxy.close()
                        }
                    }
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                println("DEBUG [ScanFragment] Camera bind failed: ${e.message}")
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    // ── Buttons ───────────────────────────────────────────────────────────────
    private fun setupButtons() {
        // Nhập tay mã vé
        binding.btnManualCheckIn.setOnClickListener {
            val code = binding.edtManualCode.text.toString()
            viewModel.onManualCheckIn(code)
            binding.edtManualCode.text?.clear()
        }

        // Quét tiếp
        binding.btnRescan.setOnClickListener {
            viewModel.onResetScan()
            binding.edtManualCode.text?.clear()
        }

        // Đăng xuất
        binding.btnLogout.setOnClickListener {
            viewModel.onLogout()
        }
    }

    // ── Observe state ─────────────────────────────────────────────────────────
    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->

                    // Tên người dùng
                    binding.txtUsername.text = "Xin chào: ${state.username}"

                    // Loading spinner
                    binding.progress.visibility =
                        if (state.isLoading) View.VISIBLE else View.GONE

                    // Scanner overlay hint
                    binding.txtScanHint.text = when {
                        state.isLoading      -> "⏳ Đang kiểm tra vé..."
                        !state.isScanEnabled -> "✅ Xong — chờ quét tiếp..."
                        else                 -> "📷 Hướng camera vào mã QR trên vé"
                    }

                    // Card kết quả
                    when (state.resultStatus) {
                        ResultStatus.IDLE -> {
                            binding.cardResult.visibility = View.GONE
                            binding.btnRescan.visibility  = View.GONE
                        }
                        ResultStatus.SUCCESS -> {
                            binding.cardResult.visibility = View.VISIBLE
                            binding.btnRescan.visibility  = View.VISIBLE

                            val r = state.lastResult!!
                            binding.cardResult.setCardBackgroundColor(
                                ContextCompat.getColor(requireContext(), R.color.result_success_bg)
                            )
                            binding.txtResultIcon.text       = "✅"
                            binding.txtResultStatus.text     = "CHECK-IN THÀNH CÔNG"
                            binding.txtResultStatus.setTextColor(
                                ContextCompat.getColor(requireContext(), R.color.result_success_text)
                            )
                            binding.txtResultEventName.text  = r.eventName
                            binding.txtResultTicketType.text = r.ticketTypeName
                            binding.txtResultCode.text       = "Mã: ${r.ticketCode}"
                            binding.txtResultUsedAt.text =
                                if (r.usedAt != null) "Dùng lúc: ${r.usedAt}" else ""
                            binding.txtResultUsedAt.visibility =
                                if (r.usedAt != null) View.VISIBLE else View.GONE
                        }
                        ResultStatus.FAIL -> {
                            binding.cardResult.visibility = View.VISIBLE
                            binding.btnRescan.visibility  = View.VISIBLE

                            binding.cardResult.setCardBackgroundColor(
                                ContextCompat.getColor(requireContext(), R.color.result_fail_bg)
                            )
                            binding.txtResultIcon.text       = "❌"
                            binding.txtResultStatus.text     = "CHECK-IN THẤT BẠI"
                            binding.txtResultStatus.setTextColor(
                                ContextCompat.getColor(requireContext(), R.color.result_fail_text)
                            )
                            binding.txtResultEventName.text  = ""
                            binding.txtResultTicketType.text = ""
                            binding.txtResultCode.text       = ""
                            binding.txtResultUsedAt.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    // ── Observe events ────────────────────────────────────────────────────────
    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is ScanUiEvent.Toast ->
                            Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()

                        is ScanUiEvent.NavigateToLogin ->
                            findNavController().navigate(
                                R.id.loginFragment,
                                null,
                                NavOptions.Builder()
                                    .setPopUpTo(R.id.root_graph, true)
                                    .build()
                            )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        _binding = null
    }
}