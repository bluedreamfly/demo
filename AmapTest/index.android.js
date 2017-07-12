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
  NativeModules,
  DeviceEventEmitter
} from 'react-native';

import Amap from './AnMap'

export default class AmapTest extends Component {

  constructor(props) {
    super(props);
    
  }

  callNative = () => {
    // NativeModules.TestModule.TestShow();
    this.map.addPoint();
  }

  componentDidMount() {
    this.map.locate((lng, lat) => {
      this.curPoint = {lng, lat};
      this.map.addCurLocation({lng, lat});
      console.log('componentDidMount', lng, lat);
    });


    DeviceEventEmitter.addListener('routeFinish', (event) => {
      console.log(event);
      let { lng, lat } = this.clickMarker;
      let list = event.list;
      list.push(`${lat},${lng}`);
      this.map.drawPolyline(list);
    })


    // let lng = 119.986721;
    // let lat = 30.263183;
    // for(let i = 0; i < 100; i++) {
    //   this.map.addPoint({
    //     lng: lng + Math.random() * 0.4,
    //     lat: lat + Math.random() * 0.4
    //   });
    // }
  }

  curClick = (event) => {
    let { lng, lat } = event.nativeEvent;
    this.clickMarker = {lng, lat};

    this.map.getPath(this.curPoint, this.clickMarker);
    console.log(this.clickMarker);
  }

  render() {
    return (
      <View style={styles.container}>
        {/*<Button title="点我调用本地方法" onPress={this.callNative} style={styles.btn}/>*/}
        <Amap ref={ref => this.map = ref} _onChange={this.curClick} style={{flex: 1, backgroundColor: '#898343'}}/>
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
