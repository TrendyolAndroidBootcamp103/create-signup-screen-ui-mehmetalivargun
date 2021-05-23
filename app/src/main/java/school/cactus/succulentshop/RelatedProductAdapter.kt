package school.cactus.succulentshop

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import school.cactus.succulentshop.product.ProductItem
import school.cactus.succulentshop.product.list.ProductAdapter

import school.cactus.succulentshop.databinding.RelatedItemProductBinding
import school.cactus.succulentshop.product.list.ProductAdapter.Companion.DIFF_CALLBACK
import school.cactus.succulentshop.product.list.ProductListFragmentDirections

class RelatedProductAdapter : ListAdapter<ProductItem, RelatedProductAdapter.RelatedProductHolder>(DIFF_CALLBACK) {
    var itemClickListener: (ProductItem) -> Unit = {}


    class RelatedProductHolder(
            private val binding: RelatedItemProductBinding,
            private val itemClickListener: (ProductItem) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductItem) {
            Log.e("error",product.title)
            binding.titleText.text = product.title
            binding.priceText.text = product.price

            Glide.with(binding.root.context)
                .load(product.imageUrl)
                .centerInside()
                .into(binding.imageView)

            binding.root.setOnClickListener {
                itemClickListener(product)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedProductHolder {
        val binding = RelatedItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )

        return RelatedProductHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: RelatedProductHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

