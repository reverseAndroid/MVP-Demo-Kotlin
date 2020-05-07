package com.xjx.mvp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment<M, A, V, P : BasePresenter<M, A, V>> : Fragment(), BaseViewImp {

    var mModel: M? = null
    var mPresenter: P? = null
    var mActivity: A? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(initContentView(), container, false)
        mModel = initModel()
        mPresenter = initPresenter()
        mPresenter?.attachView(mModel as M, mActivity as A, this as V)
        return view
    }

    abstract fun initModel(): M

    abstract fun initContentView(): Int

    abstract fun initPresenter(): P

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter!!.detachView()
        mPresenter = null
    }
}
