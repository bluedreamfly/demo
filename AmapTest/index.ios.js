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
  Animated,
  Dimensions,
  Button
} from 'react-native';
import Amap from './Amap'

const { width, height } = Dimensions.get('window');

export default class AmapTest extends Component {
  
  constructor(props) {
    super(props);
    this.state = {
      name: '',
      lat: '',
      lng: '',
      top: new Animated.Value(0)
    }
  }

  componentDidMount() {
    let lng = 119.986721;
    let lat = 30.263183;
    for(let i = 0; i < 100; i++) {
      this.map.addPoint({
        lon: lng + Math.random() * 0.1,
        lat: lat + Math.random() * 0.1
      });
    }
    setTimeout(() => {
      this.map.initMapOption({ level: 16.1, showUserLocation: true});
    }, 1000)
    
    
  }

  didSelectAnnatation = (e) => {
    console.log(e.nativeEvent);
    let { type, name, latitude, longitude } = e.nativeEvent;
    
    if (type === 'selected') {
      this.setState({
        name: name,
        lat: latitude,
        lng: longitude
      })
      this.map.getPath(this.userlocation, {lat: latitude, lng: longitude});
      // Animated.timing(this.state.top, {
      //   toValue: 1,
      //   duration: 200
      // }).start()
    } 

    if (type === 'userlocation') {
      this.userlocation = {
        lng: longitude,
        lat: latitude
      }
    }
    if(type === 'unselected') {
      Animated.timing(this.state.top, {
        toValue: 0,
        duration: 200
      }).start()
    }  

    if (type === 'clickmap') {
      this.start = {
        lat: latitude,
        lng: longitude
      }
    }

  }

  getPath = () => {
    this.map.setZoom({ level: 16.1});
    // let { lat, lng } = this.state;
    // console.log('getpath', this.start, {lat, lng});
    // this.map.getPath(this.start, {lat, lng});
  }

  render() {
    let { top, name, lng, lat } = this.state;
    let height = top.interpolate({
      inputRange: [0, 1],
      outputRange: [0, 100],
    });
    console.log('render', name);
    return (
      <View style={styles.container}>
        <Animated.View style={[styles.messageDialog, { transform: [{translateY: height}]}]}>
          <Text style={{color: '#000'}}>{name}</Text>
          <Text style={{color: '#000'}}>经度：{lng}</Text>
          <Text style={{color: '#000'}}>纬度：{lat}</Text>
        </Animated.View>
        <View style={styles.btn}><Button  title="点我规划路线" onPress={this.getPath}/></View>
        <Amap ref={_ref => this.map = _ref} style={{width: width, height: height}} didSelectAnnatation={this.didSelectAnnatation}/>
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
  messageDialog: {
    position: 'absolute',
    zIndex: 100000,
    top: -100,
    height: 100,
    width: width,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#fff'
  },
  btn: {
    position: 'absolute',
    bottom: 0,
    zIndex: 100000
  }
});

AppRegistry.registerComponent('AmapTest', () => AmapTest);
