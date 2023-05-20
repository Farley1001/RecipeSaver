package com.farware.recipesaver.feature_recipe.presentation.permissions

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class CameraPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "You have permanently declined the Camera permission.\nYou will need to go to the app settings to grant it."
        } else {
            "This app needs permission to use the Camera."
        }
    }
}

class RecordAudioPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "You have permanently declined the Microphone permission.\nYou will need to go to the app settings to grant it."
        } else {
            "This app needs permission to use the Microphone to record audio."
        }
    }
}

class PhoneCallPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "You have permanently declined the Phone Calling permission.\nYou will need to go to the app settings to grant it."
        } else {
            "This app needs Phone Calling permission."
        }
    }
}

class LocationPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "You have permanently declined the Location permission.\nYou will need to go to the app settings to grant it."
        } else {
            "This app needs the Location permission."
        }
    }
}

class BluetoothPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "You have permanently declined the Bluetooth permission.\nYou will need to go to the app settings to grant it."
        } else {
            "This app needs Bluetooth permission."
        }
    }
}