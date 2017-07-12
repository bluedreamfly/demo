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

import Amap from './AnMap';

export default class AmapTest extends Component {
  constructor(props) {
    super(props);
  }

  callNative = () => {
    // NativeModules.TestModule.TestShow();
    this.map.addPoint();
  };

  componentDidMount() {
    this.map.locate((lng, lat) => {
      this.curPoint = { lng, lat };
      this.map.addCurLocation({ lng, lat });
      this.addBikes(lng, lat);
      console.log('componentDidMount', lng, lat);
    });

    DeviceEventEmitter.addListener('routeFinish', event => {
      console.log(event);
      let { lng, lat } = this.clickMarker;
      let list = event.list;
      list.push(`${lat},${lng}`);
      this.map.drawPolyline(list);
    });
  }

  addBikes(lng, lat) {
    let bikes = [];
    for (let i = 0; i < 30; i++) {
      bikes.push({
        lng: i > 15 ? lng + Math.random() * 0.01 : lng - Math.random() * 0.01,
        lat: i > 15 ? lat + Math.random() * 0.01 : lat - Math.random() * 0.01
      });
    }
    this.map.addPoints(bikes);
  }

  curClick = event => {
    let { lng, lat } = event.nativeEvent;
    this.clickMarker = { lng, lat };

    this.map.getPath(this.curPoint, this.clickMarker);
    console.log(this.clickMarker);
  };

  curLocationChange = (event) => {
    let { lng, lat } = event.nativeEvent;
    console.log(lng, lat);
    this.addBikes(lng, lat);
  }

  render() {
    return (
      <View style={styles.container}>
        {/*<Button title="点我调用本地方法" onPress={this.callNative} style={styles.btn}/>*/}
        <Amap
          ref={ref => (this.map = ref)}
          _onChange={this.curClick}
          _onCurLocationChange={this.curLocationChange}
          style={{ flex: 1, backgroundColor: '#898343' }}
        />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    // justifyContent: 'center',
    // alignItems: 'center',
    backgroundColor: '#F5FCFF'
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5
  },
  btn: {
    position: 'absolute',
    bottom: 0,
    zIndex: 100000
  }
});

AppRegistry.registerComponent('AmapTest', () => AmapTest);
