package school.cactus.succulentshop.product.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import school.cactus.succulentshop.R
import school.cactus.succulentshop.RelatedProductAdapter
import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.api.product.Product
import school.cactus.succulentshop.api.product.RelatedProducts
import school.cactus.succulentshop.databinding.FragmentProductDetailBinding
import school.cactus.succulentshop.product.list.ProductDecoration
import school.cactus.succulentshop.product.list.ProductListFragmentDirections
import school.cactus.succulentshop.product.toProductItem
import school.cactus.succulentshop.product.toProductItemList

class ProductDetailFragment : Fragment() {
    private var _binding: FragmentProductDetailBinding? = null

    private val binding get() = _binding!!
    private val  adapter= RelatedProductAdapter()

    private val args: ProductDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.app_name)
        binding.recyclerView.adapter = adapter
        //binding.recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)

        binding.recyclerView.layoutManager= LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        binding.recyclerView.addItemDecoration(RelatedProductDecoration())

        adapter.itemClickListener = {

            val action = ProductDetailFragmentDirections.openRelatedProduct(it.id)
            findNavController().navigate(action)
        }
        fetchProduct()
    }

    private fun fetchProduct() {
        Log.e("deneme",args.productId.toString())
        api.getProductById(args.productId).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                when (response.code()) {
                    200 -> onSuccess(response.body()!!)
                    401 -> onTokenExpired()
                    else -> onUnexpectedError()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Snackbar.make(
                    binding.root, R.string.check_your_connection,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(R.string.retry) {
                    fetchProduct()
                }.show()
            }
        })

        api.getRelatedProducts(args.productId).enqueue(object :Callback<RelatedProducts> {

            override fun onResponse(
                call: Call<RelatedProducts>,
                response: Response<RelatedProducts>
            ) {
                when (response.code()) {
                    200 -> onSuccess(response.body()!!)
                    401 -> onTokenExpired()
                    else -> onUnexpectedError()
                }
            }



            override fun onFailure(call: Call<RelatedProducts>, t: Throwable) {
                Snackbar.make(
                    binding.root, R.string.check_your_connection,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(R.string.retry) {
                    fetchProduct()
                }.show()
            }


        })
    }

    private fun onSuccess(product:Product) {
        val productItem = product.toProductItem()

        binding.apply {
            titleText.text = productItem.title
            priceText.text = productItem.price
            descriptionText.text = productItem.description

            Glide.with(binding.root)
                .load(productItem.highResImageUrl)
                .into(imageView)
        }
    }

    private fun onSuccess(listOfProducts:  RelatedProducts) {
       val products=listOfProducts.products
        if (products!!.isEmpty()){
            Log.e("Product","Product yok")
            binding.recyclerView.visibility=View.INVISIBLE
            binding.relatedText.visibility=View.INVISIBLE
        }
        adapter.submitList(products!!.toProductItemList())


    }

    private fun onTokenExpired() {
        Snackbar.make(
            binding.root, R.string.your_session_is_expired,
            Snackbar.LENGTH_INDEFINITE
        ).setAction(R.string.log_in) {
            navigateToLogin()
        }.show()
    }

    private fun navigateToLogin() = findNavController().navigate(R.id.tokenExpired)

    private fun onUnexpectedError() {
        Snackbar.make(
            binding.root, R.string.unexpected_error_occurred,
            BaseTransientBottomBar.LENGTH_LONG
        ).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}