import { NativeModules } from "react-native";

const { CustomModule } = NativeModules;


export function gotoNativePhotoCapture() {
    CustomModule.goToNativeActivity()
}

export function getPhotoBase64() {
    return CustomModule.getPhotoBase64()
    
}