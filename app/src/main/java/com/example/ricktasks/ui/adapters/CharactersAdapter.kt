package com.example.ricktasks.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ricktasks.databinding.ItemCharacterBinding
import com.example.ricktasks.data.remote.model.Character

class CharactersAdapter(
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
        }
    }
}
