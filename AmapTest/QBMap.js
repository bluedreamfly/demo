import { requireNativeComponent, View, NativeModules, findNodeHandle } from 'react-native';
import React from 'react'
import PropTypes from 'prop-types';

class Amap extends React.Component {
  render() {
    return (
      <RNAmap 
        ref={(ref) => this.ref = ref}
        {...this.props}
      />
    )
  }
}
Amap.propTypes = {
  ...View.propTypes,
  onMapLoaded: PropTypes.func,
  onMarkerPress: PropTypes.func,
  onCurrentLocationChange: PropTypes.func
};
const RNAmap = requireNativeComponent('QBMap', Amap, {
  nativeOnly: {
    onMapLoaded: PropTypes.func,
    onMarkerPress: PropTypes.func,
    onCurrentLocationChange: PropTypes.func
  }
});

module.exports = Amap;