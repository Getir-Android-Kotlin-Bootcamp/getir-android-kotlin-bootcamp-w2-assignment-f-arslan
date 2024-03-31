package com.getir.patika.foodmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Abstract base fragment that provides a standardized way to use view binding in fragments.
 *
 * This class handles the lifecycle of the view binding, ensuring it is cleared when the view is destroyed to prevent memory leaks.
 *
 * @param T The type of the view binding associated with the fragment.
 */
abstract class BaseFragment<T : ViewBinding> : Fragment() {
    private var _binding: T? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    /**
     * Abstract method that must be implemented by subclasses to provide the view binding.
     *
     * @param inflater The LayoutInflater object that can be used to inflate the view binding.
     * @param container The parent view that the fragment's UI should be attached to.
     * @return The initialized view binding for the fragment.
     */
    protected abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): T

    /**
     * Abstract method that must be implemented by subclasses to initialize their views.
     */
    protected abstract fun initializeViews()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
