/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  Button,
  NativeModules
} from 'react-native';

import Amap from './AnMap'

export default class AmapTest extends Component {


  callNative = () => {
    // NativeModules.TestModule.TestShow();
    this.map.addPoint();
  }

  componentDidMount() {
    let lng = 119.986721;
    let lat = 30.263183;
    for(let i = 0; i < 100; i++) {
      this.map.addPoint({
        lng: lng + Math.random() * 0.1,
        lat: lat + Math.random() * 0.1
      });
    }
  }

  render() {
    return (
      <View style={styles.container}>
        <Button title="点我调用本地方法" onPress={this.callNative} style={styles.btn}/>
        {/*<Text style={styles.instructions}>
          To get started, edit index.android.js
        </Text>
        <Text style={styles.instructions}>
          Double tap R on your keyboard to reload,{'\n'}
          Shake or press menu button for dev menu
        </Text>*/}

        <Amap ref={ref => this.map = ref} style={{flex: 1, backgroundColor: '#898343'}}/>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    // justifyContent: 'center',
    // alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
  btn: {
    position: 'absolute',
    bottom: 0,
    zIndex: 100000
  }
});

AppRegistry.registerComponent('AmapTest', () => AmapTest);
