import { NativeModules } from "react-native";

const { CustomModule } = NativeModules;


// ouverture l'activité avec la prise de photo
export function gotoNativePhotoCapture() {
    CustomModule.goToNativeActivity()
}

// recuperation de l'image stockée
export function getPhotoBase64() {
    return CustomModule.getPhotoBase64()
    
}

// suppression de la data
export function clearPhoto() {
    return CustomModule.clearPhotoBase64()
}
