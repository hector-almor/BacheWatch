package com.loswachabaches.bachewatch.ui.screens.mainscreen.tabs

import android.view.View
import android.widget.TextView
import org.osmdroid.library.R
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow

class BacheInfoWindow(mapView: MapView) : MarkerInfoWindow(R.layout.bonuspack_bubble, mapView) {

    override fun onOpen(item: Any?) {
        super.onOpen(item)
        val marker = item as Marker

        // Título
        mView.findViewById<TextView>(R.id.bubble_title)?.apply {
            text = marker.title
            setTextColor(android.graphics.Color.parseColor("#1A1A2E"))
        }

        // Descripción
        mView.findViewById<TextView>(R.id.bubble_description)?.apply {
            text = marker.snippet
            setTextColor(android.graphics.Color.parseColor("#4B4A4A"))
            visibility = if (marker.snippet.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        // Ocultar el botón de acción que viene por defecto
        mView.findViewById<View>(R.id.bubble_moreinfo)?.visibility = View.GONE
    }
}