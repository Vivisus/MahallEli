package com.example.mahalleli.anasayfadosyalari

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mahalleli.LoginPage
import com.example.mahalleli.R
import com.example.mahalleli.adapterdosyalari.ProfilAdapter
import com.example.mahalleli.adapterdosyalari.ProfileItem
import com.example.mahalleli.databinding.FragmentKullaniciBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Kullanici : Fragment() {
    private var _binding: FragmentKullaniciBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKullaniciBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Kullanıcı oturum açmışsa veriyi çek ve UI'ı güncelle
            fetchUserData(currentUser.uid)
        } else {
            // Kullanıcı oturum açmamışsa giriş sayfasına yönlendir
            val intent = Intent(requireContext(),LoginPage::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    private fun fetchUserData(userId: String) {
        db.collection("users").document(userId).get().addOnSuccessListener { document ->
            if (document != null) {
                // Firestore'dan verileri al
                val userName = document.getString("userName") ?: "Kullanıcı Adı Yok"
                val bio = document.getString("bio") ?: "Biyografi yok"
                val isSeller = document.getBoolean("isSeller") ?: false
                val avatarUrl = document.getString("avatarUrl") ?: ""

                // Verilerle UI'ı güncellemek için fonksiyonu çağır
                updateUI(userName, bio, isSeller, avatarUrl)
            }
        }.addOnFailureListener { exception ->
            println("Hata oluştu: $exception")
        }
    }

    private fun updateUI(userName: String, bio: String, isSeller: Boolean, avatarUrl: String) {
        val profilItems = mutableListOf<ProfileItem>()
        profilItems.add(ProfileItem.UserInfo(
            avatarUrl = avatarUrl,
            userName = userName,
            bio = bio
        ))
        profilItems.add(ProfileItem.UserActions(actionText = "Ayarlar"))
        if (isSeller) {
            profilItems.add(ProfileItem.SellerReviews(reviews = listOf("Harika bir satıcı!", "İletişimi çok iyi.")))
        } else {
            profilItems.add(ProfileItem.UserActions(actionText = "Satıcı Olmak İstiyorum"))
        }
        profilItems.add(ProfileItem.Logout(logoutText = "Çıkış Yap"))

        val adapter = ProfilAdapter(profilItems) { clickedItem ->
            when (clickedItem) {
                is ProfileItem.UserActions -> {
                    when (clickedItem.actionText) {
                        "Ayarlar" -> { findNavController().navigate(R.id.action_kullanici_to_settingsFragment ) }
                        "Profil Bilgileri" -> { findNavController().navigate(R.id.action_kullanici_to_profileDetailsFragment)}
                        "Satıcı Olmak İstiyorum" -> { findNavController().navigate(R.id.action_kullanici_to_becomeSellerFragment) }
                    }
                }
                is ProfileItem.Logout -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(requireContext(),LoginPage::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                is ProfileItem.SellerOption -> {}
                is ProfileItem.SellerReviews -> findNavController().navigate(R.id.action_kullanici_to_sellerReviewsFragment)
                    is ProfileItem.UserInfo -> {}
                else -> {}
            }
        }
        binding.kullaniciRecycler.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}