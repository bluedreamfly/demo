import React, { Component } from 'react';
import { View, Text, StyleSheet, InteractionManager, ToastAndroid, Image } from 'react-native';
import QBMap from './QBMap';
import QBMarker from './QBMarker';
const resolveAssetSource = require('resolveAssetSource');
const markers = [
  { lat: 39.798449, lng: 116.379772, }, //title: '1111' },
  { lng: 116.613918, lat: 39.921785, }, //title: '2222' },
  { lng: 116.208798, lat: 39.824821, }, //title: '3333' },
  { lng: 116.497189, lat: 39.719271, }, //title: '4444' }
];

// const image = ;

export default class QBMapPage extends Component {

  static navigationOptions = {
    title: '地图页面重构',
  };

  constructor(props) {
    super(props);
    this.state = {
      showMap: false
    };
  }

  componentDidMount() {

    // console.log('componentDidMount', require('./images/ic_bike.png'));

    InteractionManager.runAfterInteractions(() => {
      this.setState({
        showMap: true
      });
    });
  }

  handleMapLoad = () => {
    ToastAndroid.show('map is loaded', 1000)
    // console.log('handleMapLoad.........sdfsdf');
    // this.setState({})
    // this.setState({showMap: true})
  };

  handleMarkerPress = (event) => {
    console.log(event.nativeEvent);
  }

  handleLocationChange = (event) => {
    console.log(event.nativeEvent);
  }

  render() {
    const { showMap } = this.state;
    // console.log()
    return (
      <View style={styles.container}>
        {showMap &&
          <QBMap
            style={{ flex: 1, backgroundColor: '#898343' }}
            onMapLoaded={this.handleMapLoad}
            onMarkerPress={this.handleMarkerPress}
            onCurrentLocationChange={this.handleLocationChange}
          >
            {markers.map((marker, index) => {
              let { lat, lng, title } = marker;
              return (
                <QBMarker
                  image={require('./images/ic_bike.png')}
                  coordinate={{ lat, lng }}
                  key={index}
                  draggable
                  title={title}
                />
              );
            })}
          </QBMap>}
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F5FCFF'
  },
  test: {
    position: 'absolute',
    bottom: 20,
    width: 100,
    height: 30,
    backgroundColor: 'red',
    zIndex: 1000
  }
});
