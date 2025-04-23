package com.example.ricktasks.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ricktasks.R
import com.example.ricktasks.data.remote.model.Character
import com.example.ricktasks.data.repository.PreferencesRepository
import com.example.ricktasks.databinding.ItemCharacterBinding

class CharactersAdapter(
    private val preferencesRepository: PreferencesRepository,
    private var characters: List<Character> = listOf()
) : RecyclerView.Adapter<CharactersAdapter.CharacterViewHolder>() {

    fun submitList(newList: List<Character>) {
        characters = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun getItemCount(): Int = characters.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characters[position])
    }

    inner class CharacterViewHolder(private val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character) {
            binding.textName.text = character.name
            binding.textSpecies.text = character.species
            binding.textStatus.text = character.status

            Glide.with(binding.root.context)
                .load(character.image)
                .into(binding.imageCharacter)

            val isFav = preferencesRepository.isFavorite(character.id)
            updateFavIcon(isFav)

            binding.btnFav.setOnClickListener {
                preferencesRepository.toggleFavorite(character.id)
                val newFavState = preferencesRepository.isFavorite(character.id)
                updateFavIcon(newFavState)
                notifyDataSetChanged()
            }
        }

        private fun updateFavIcon(isFav: Boolean) {
            if (isFav) {
                binding.btnFav.alpha = 1.0f
                binding.btnFav.imageTintList = null
            } else {
                binding.btnFav.alpha = 0.2f
                binding.btnFav.imageTintList = ContextCompat.getColorStateList(binding.root.context, R.color.md_theme_secondaryContainer_mediumContrast)
            }
        }
    }
}

