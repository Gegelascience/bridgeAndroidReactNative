import { NativeModules } from "react-native";

const { CustomModule } = NativeModules;


export function gotoNativePhotoCapture() {
    CustomModule.goToNativeActivity()
}