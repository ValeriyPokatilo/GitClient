package app.xl.androidapp.presentation.repositories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.xl.androidapp.databinding.RepositoryItemBinding
import app.xl.androidapp.domain.entity.Repo

class RepoAdapter(
    private val onItemClick: (Repo) -> Unit
) : ListAdapter<Repo, RepoAdapter.RepoViewHolder>(DiffCallback()) {

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
        private val onItemClick: (Repo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(repo: Repo) = with(binding) {
            repositoryName.text = repo.name
            repositoryLanguage.text = repo.language

            repositoryDescription.apply {
                text = repo.description
                isVisible = !repo.description.isNullOrBlank()
            }

            root.setOnClickListener {
                onItemClick(repo)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Repo>() {
        override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean =
            oldItem == newItem
    }
}