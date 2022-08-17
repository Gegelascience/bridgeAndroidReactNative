/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, {useEffect, useState} from 'react';

import {
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
  Button,
  AppState,
  Image
} from 'react-native';



import {gotoNativePhotoCapture, getPhotoBase64} from "./bridgeJs/myNativeFunction"

import {
  Colors,
  Header,
} from 'react-native/Libraries/NewAppScreen';


const App = () => {
  const isDarkMode = useColorScheme() === 'dark';
  const [base64Photo, setBase64Photo] =  useState("");

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  var eventListenerSubscription;

  const _handleAppStateChange = nextAppState => {
    // si l'activité react est en focus on recupère la potentielle photo
    if (nextAppState === 'active') {
      var test =getPhotoBase64()
      if (test.length > 0) {
        //console.log("test", "photo ready", test.length)
        setBase64Photo("data:image/png;base64,"+test)
      } else {
        setBase64Photo("")
      }
      
    }

    
  };


  useEffect(() => {

    //on mount
    eventListenerSubscription = AppState.addEventListener('change', _handleAppStateChange);
    return () => {
      // on unmount
      eventListenerSubscription.remove();
    };
  }, []);

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={backgroundStyle}>
        <Header />
        <View style={styles.container} >
          <Button title='Test' onPress={() => {
            gotoNativePhotoCapture()
          }}/>
          { (base64Photo.length > 0) &&
            <Image style={{width: 250, height: 450, resizeMode: "cover", borderWidth: 1, borderColor: 'red'}} source={{uri: base64Photo}}/>  
          }
          
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    width:"100%",
    height:500,
    backgroundColor:"blue"

  }
});

export default App;
