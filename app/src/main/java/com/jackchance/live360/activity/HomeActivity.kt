package com.jackchance.live360.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.jackchance.live360.R
import com.jackchance.live360.util.toMainActivity
import com.jackchance.live360.util.viewById

class HomeActivity : BaseActivity(), View.OnClickListener {

    private val myMiscButton: Button by viewById(R.id.my_misc_buttom)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        myMiscButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.my_misc_buttom -> {
                toMainActivity()
            }
        }
    }
}
