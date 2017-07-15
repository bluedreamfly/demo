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
  Dimensions,
  InteractionManager,
  DeviceEventEmitter
} from 'react-native';

import Amap from './AnMap';

const { width, height } = Dimensions.get('window');

export default class MapPage extends Component {

  static navigationOptions = {
    title: '地图页面',
  };
  constructor(props) {
    super(props);
  }

  locationNum = 0;

  state = {
    isShowMap: false,
    height: 0
  }
  callNative = () => {
    // NativeModules.TestModule.TestShow();
    this.map.addPoint();
  };

  componentDidMount() {
    //如果一开始就展示地图，会把卡住页面切换动画
    InteractionManager.runAfterInteractions(() => {
      this.setState({
        isShowMap: true
      })
      this.map.locate((lng, lat) => {
        this.curPoint = { lng, lat };
        this.map && this.map.addCurLocation({ lng, lat });
      });

      DeviceEventEmitter.addListener('routeFinish', this.drawPolyline);
    });
  }

  drawPolyline = (event) => {
    let { lng, lat } = this.clickMarker;
    let list = event.list;
    list.push(`${lat},${lng}`);
    this.map.drawPolyline(list);
  }

  componentWillUnmount() {
    // clearTimeout(this.timer);
    clearTimeout(this.timer1);
    this.locationNum = 0;
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
    if (this.locationNum > 0) {
      this.addBikes(lng, lat);
    } 
    this.locationNum++;
  }

  render() {
    const { isShowMap, height } = this.state;
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

