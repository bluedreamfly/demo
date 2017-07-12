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

export default class MapPage extends Component {

  static navigationOptions = {
    title: '地图页面',
  };
  constructor(props) {
    super(props);
  }

  state = {
    isShowMap: false
  }
  callNative = () => {
    // NativeModules.TestModule.TestShow();
    this.map.addPoint();
  };

  componentDidMount() {

    this.timer = setTimeout(() => {
      this.setState({
        isShowMap: true
      })
    }, 300)

    this.timer1 = setTimeout(() => {
      
      this.map.locate((lng, lat) => {
        this.curPoint = { lng, lat };
        this.map.addCurLocation({ lng, lat });
        this.addBikes(lng, lat);
      });

      DeviceEventEmitter.addListener('routeFinish', this.drawPolyline);
    }, 320)
  }

  drawPolyline = (event) => {
    let { lng, lat } = this.clickMarker;
    let list = event.list;
    list.push(`${lat},${lng}`);
    this.map.drawPolyline(list);
  }

  componentWillUnmount() {
    clearTimeout(this.timer);
    clearTimeout(this.timer1);
    DeviceEventEmitter.removeListener('routeFinish', this.drawPolyline)
    this.map.remove();
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
    const { isShowMap } = this.state;
    return (
      <View style={styles.container}>
        {/*<Button title="点我调用本地方法" onPress={this.callNative} style={styles.btn}/>*/}
        { isShowMap && <Amap
          ref={ref => (this.map = ref)}
          _onChange={this.curClick}
          _onCurLocationChange={this.curLocationChange}
          style={{ flex: 1, backgroundColor: '#898343' }}
        /> }
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

