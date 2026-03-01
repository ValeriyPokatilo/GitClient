package app.xl.androidapp.presentation.repositories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.xl.androidapp.R
import app.xl.androidapp.databinding.RepositoryItemBinding
import app.xl.androidapp.domain.entity.Repository

class RepoAdapter(
    private val onItemClick: (Repository) -> Unit
) : ListAdapter<Repository, RepoAdapter.RepoViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = RepositoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RepoViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RepoViewHolder(
        private val binding: RepositoryItemBinding,
        private val onItemClick: (Repository) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(repository: Repository) = with(binding) {
            repositoryName.text = repository.name
            repositoryLanguage.text = repository.language
            repositoryLanguage.setTextColor(
                repository.languageColor ?: R.color.white
            )

            repositoryDescription.apply {
                text = repository.description
                isVisible = !repository.description.isNullOrBlank()
            }

            root.setOnClickListener {
                onItemClick(repository)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Repository>() {
        override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean =
            oldItem == newItem
    }
}