@file:Suppress("DEPRECATION")

package com.example.guceats.VoiceOverIP



import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.rtp.AudioCodec
import android.net.rtp.AudioGroup
import android.net.rtp.AudioStream
import android.net.rtp.RtpStream
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.StrictMode
import android.text.format.Formatter
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.guceats.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.net.InetAddress


class VoIP : AppCompatActivity() {

    // init requierd objects
    lateinit var m_AudioGroup: AudioGroup
    lateinit var m_AudioStream: AudioStream
    lateinit var srcIP: TextView
    lateinit var srcPort: TextView
    var localIp = StringBuilder()
    lateinit var connect: ImageButton
    lateinit var disconnect: ImageButton
    var permissionGranted = false
    val REQUEST_CODE = 100
    // get the string ip
    private val localIPAddress: ByteArray
        get() {
            var bytes: ByteArray? = null
            try {
                // get the string ip
                val wm = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
                val ip = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)

                // convert to bytes
                var inetAddress: InetAddress? = null
                try {
                    inetAddress = InetAddress.getByName(ip)
                } catch (e: Exception) {
                    Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
                }
                bytes = ByteArray(0)
                if (inetAddress != null) {
                    bytes = inetAddress.address
                }
            } catch (e: Exception) {
                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
            for (b: Byte in bytes!!) localIp.append((b.toUByte() and 0xFF.toUByte()).toString() + ".")

            return bytes
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voip)
        showDialog()

        // Check if the permissions are granted or not
        permissionGranted = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        if (!permissionGranted) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_CODE
            )
        }
        if (!permissionGranted) return
        srcIP = findViewById(R.id.dynamicIpText)
        srcPort = findViewById(R.id.dynamicPortText)
        connect = findViewById(R.id.connectBtn)
        disconnect = findViewById(R.id.disconnectBtn)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            val audio = getSystemService(AUDIO_SERVICE) as AudioManager
            audio.mode = AudioManager.MODE_IN_COMMUNICATION
            m_AudioGroup = AudioGroup()
            m_AudioGroup.mode = AudioGroup.MODE_NORMAL
            m_AudioStream = AudioStream(InetAddress.getByAddress(localIPAddress))
            val localPort = m_AudioStream.localPort
            Toast.makeText(this, localPort.toString() , Toast.LENGTH_LONG).show()
            srcPort.text = localPort.toString() + ""
            srcIP.text = localIp.deleteCharAt(localIp.length - 1).toString()
            m_AudioStream.codec = AudioCodec.PCMU
            m_AudioStream.mode = RtpStream.MODE_NORMAL
            disconnect.isEnabled = false
            connect.setOnClickListener {
                val remoteAddress =
                    (findViewById<View>(R.id.destIpText) as EditText).text.toString()
                val remotePort = (findViewById<View>(R.id.destPortText) as EditText).text.toString()
                println("$remoteAddress/$remotePort")

                    try {
                        m_AudioStream.associate(
                            InetAddress.getByName(remoteAddress),
                            remotePort.toInt()
                        )
                        connect.isEnabled = false
                        disconnect.isEnabled = true
                        m_AudioStream.join(m_AudioGroup)
                       // Toast.makeText(this,"Associated",Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
                        Toast.makeText(this,"not Associated",Toast.LENGTH_SHORT).show()
                        connect.isEnabled = true
                        disconnect.isEnabled = false
                    }

            }
            disconnect.setOnClickListener { m_AudioStream.release() }
        } catch (e: Exception) {
            Log.e("----------------------", e.toString())
            e.printStackTrace()
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun showDialog() {
        val alertDialogBuilder = MaterialAlertDialogBuilder(this)
        alertDialogBuilder.setTitle("Help")
        alertDialogBuilder.setIcon(R.drawable.help)
        alertDialogBuilder.background =
            resources.getDrawable(R.drawable.rounded_edit_text, null)
        alertDialogBuilder.setMessage(
            "1- Both sides should make sure that they gave the application the permission is asked for.\n" +
                    "2- Both sides should be in the same network. \n" +
                    "3- You should swap your local ip and ports appearing on the top of your screen.\n" +
                    "4- Both of you should click the green button to establish the connection.\n" +
                    "5- When any one needs to terminate they should simply click on the red button."
        )
        alertDialogBuilder.setNegativeButton(
            "Dismiss"
        ) { dialogInterface, i -> dialogInterface.dismiss() }
        alertDialogBuilder.show()
    }
}
