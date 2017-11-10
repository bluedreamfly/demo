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
  DeviceEventEmitter,
  
} from 'react-native';

import asyncTask from './serviceTask'

import { StackNavigator } from 'react-navigation';
import MapPage from './mapPage'
import QBMapPage from './QBMapPage'

class HomeScreen extends React.Component {
  static navigationOptions = {
    title: '首页',
  };

  pushMap = () => {
    const { navigate } = this.props.navigation;
    navigate('qbmap');
  }

  startService = () => {
    NativeModules.AmapModule.startTestServive();
  }
  render() {
    return <View style={styles.container}>
      <Button onPress={this.pushMap} title="地图页面" />
      {/*<Button onPress={this.startService} title="启动服务" style={{padding: 100}} />*/}
    </View>
  }
}

const SimpleApp = StackNavigator({
  Home: { screen: HomeScreen },
  map: { screen: MapPage},
  qbmap: {screen: QBMapPage}
});

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center'
  }
})

AppRegistry.registerComponent('AmapTest', () => SimpleApp);


AppRegistry.registerHeadlessTask('SomeTaskName', () => asyncTask);
