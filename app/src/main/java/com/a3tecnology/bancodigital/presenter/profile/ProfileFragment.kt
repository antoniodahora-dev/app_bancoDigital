package com.a3tecnology.bancodigital.presenter.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.a3tecnology.bancodigital.R
import com.a3tecnology.bancodigital.data.model.User
import com.a3tecnology.bancodigital.databinding.FragmentProfileBinding
import com.a3tecnology.bancodigital.databinding.LayoutBottomSheetImageBinding
import com.a3tecnology.bancodigital.util.BaseFragment
import com.a3tecnology.bancodigital.util.FirebaseHelp
import com.a3tecnology.bancodigital.util.StateView
import com.a3tecnology.bancodigital.util.initToolbar
import com.a3tecnology.bancodigital.util.showBottomSheet
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()
    private var user : User? = null

    private var imageProfile: String? = null
    private var currentPhotoPath: String? = null

    private val tagPicasso = "tagPicasso"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        getProfile()
        initListeners()
    }

    private fun saveProfile(urlString: String? = null) {
      user?.let {

         if (urlString != null) {
             it.image = urlString
         }

          profileViewModel.saveProfile(it).observe(viewLifecycleOwner) { stateView ->
              when (stateView) {

                  is StateView.Loading -> {
                      binding.progressBar.isVisible = true
                  }

                  is StateView.Success -> {
                      binding.progressBar.isVisible = false
                      Toast.makeText(requireContext(), R.string.message_perfil_save_success,
                          Toast.LENGTH_SHORT).show()
                  }

                  is StateView.Error -> {
                      binding.progressBar.isVisible = false
                      showBottomSheet(
                          message = getString(
                              FirebaseHelp.validatorError(
                                  stateView.message ?: ""
                              )
                          )
                      )
                  }
              }
          }
      }
    }

    private fun saveImageProfile() {
        imageProfile?.let { image ->
            profileViewModel.saveImageProfile(image).observe(viewLifecycleOwner) { stateView ->
                when(stateView) {
                    is StateView.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is StateView.Success -> {
                        binding.progressBar.isVisible = false
                        saveProfile(stateView.data)

                    }
                    is StateView.Error -> {
                        binding.progressBar.isVisible = false
                        showBottomSheet(message = stateView.message)
                    }
                }

            }
        }
    }

    private fun getProfile() {
        profileViewModel.getProfile().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {

                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressBar.isVisible = false
                    stateView.data?.let {
                        user = it
                    }
                    configData()
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(
                        message = getString(
                            FirebaseHelp.validatorError(
                                stateView.message ?: ""
                            )
                        )
                    )
                }
            }
        }
    }

    private fun initListeners() {
        binding.imgHomeUser.setOnClickListener{
            showBottomSheetImage()
        }

        binding.btnSavePerfil.setOnClickListener {
            if (user != null) {
                if (imageProfile != null) {
                    saveImageProfile()
                } else {
                    validatorData()
                }
            }
        }
    }

    private fun showBottomSheetImage() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        val bottomSheetBinding: LayoutBottomSheetImageBinding =
            LayoutBottomSheetImageBinding.inflate(layoutInflater, null, false)

        bottomSheetBinding.btnCameraBottomSheet.setOnClickListener {
            checkPermissionCamera()
            bottomSheetDialog.dismiss()
        }

        bottomSheetBinding.btnGalleryBottomSheet.setOnClickListener {
            checkPermissionGallery()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    private fun checkPermissionCamera() {

        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
//                Toast.makeText(requireContext(), "Permissão Concedida", Toast.LENGTH_SHORT).show()
                openCamera()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    requireContext(),
                    "Permissão Negada",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        showDialogPermissionDenied(
            permissionlistener = permissionlistener,
            permission = Manifest.permission.CAMERA,
            message = R.string.message_perfil_access_denied_camera
        )

    }

    private fun checkPermissionGallery() {
        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(requireContext(), "Permissão Concedida", Toast.LENGTH_SHORT).show()
                openGallery()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    requireContext(),
                    "Permissão Negada",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        showDialogPermissionDenied(
            permissionlistener = permissionlistener,
            permission = Manifest.permission.READ_EXTERNAL_STORAGE,
            message = R.string.message_perfil_access_denied_gallery
        )
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val file = File(currentPhotoPath!!)
            binding.imgHomeUser.setImageURI(Uri.fromFile(file))

            imageProfile = file.toURI().toString()
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        var photoFile: File? = null

        try {

            photoFile = createImageFile()

        } catch (ex: IOException) {
            Toast.makeText(
                requireContext(),
                "Não foi possível abrir a câmera do dispositivo",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (photoFile != null) {
            val photoURI = FileProvider.getUriForFile(
                requireContext(),
                "com.a3tecnology.bancodigital.fileprovider",
                photoFile
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            cameraLauncher.launch(takePictureIntent)
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss",
            Locale("pt", "BR")).format(Date())

        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        currentPhotoPath = image.absolutePath
        return image
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {

            val imageSelected = result.data!!.data
            imageProfile = imageSelected.toString()

            if (imageSelected != null) {
                binding.imgHomeUser.setImageBitmap(getBitmap(imageSelected))
            }

        }
    }

    private fun getBitmap(pathUri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, pathUri)
            } else {
                val source =
                    ImageDecoder.createSource(requireActivity().contentResolver, pathUri)
                ImageDecoder.decodeBitmap(source)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    private fun showDialogPermissionDenied(
        permissionlistener: PermissionListener,
        permission: String,
        message: Int
    ) {
        TedPermission.create()
            .setPermissionListener(permissionlistener)
            .setDeniedTitle("Permissão Negada")
            .setDeniedMessage(message)
            .setDeniedCloseButtonText("Não")
            .setGotoSettingButtonText("Sim")
            .setPermissions(permission)
            .check();
    }

    private fun validatorData() {
        val name = binding.editPerfilNome.text.toString().trim()
        val phone = binding.editPerfilPhone.unMaskedText // using mask phone

        if (name.isNotEmpty()) {
                if (phone?.isNotEmpty() == true) { // verifica a condição do campo da mascara
                    if (phone.length == 11) {

                        user?.name = name
                        user?.phone = phone

                        hideKeyboard()
                        saveProfile()
                } else {
                        showBottomSheet(message = getString(R.string.msg_phone_register_invalid))
                    }
                } else {
                    showBottomSheet(message = getString(R.string.txt_phone_empty))
                }
        } else {
            showBottomSheet(message = getString(R.string.txt_name_empty))
        }
    }

    private fun configData() {

        if (user?.image == "") {
            binding.progressBarImg.isVisible = false
            binding.imgHomeUser.isVisible = true
            binding.imgHomeUser.setImageResource(R.drawable.ic_user_place_holder)
        } else {
            Picasso.get()
                .load(user?.image)
                .tag(tagPicasso)
                .fit()
                .centerCrop()
                .rotate(90f)
                .into(binding.imgHomeUser, object : Callback {
                    override fun onSuccess() {
                        binding.imgHomeUser.isVisible = true
                        binding.progressBarImg.isVisible = false
                    }

                    override fun onError(e: java.lang.Exception?) {
                    }
                })
        }

        binding.editPerfilNome.setText(user?.name)
        binding.editPerfilPhone.setText(user?.phone)
        binding.editPerfilEmail.setText(user?.email)

    }

    override fun onDestroy() {
        super.onDestroy()
        Picasso.get().cancelTag(tagPicasso)
        _binding = null

    }
}