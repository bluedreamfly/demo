import { requireNativeComponent, View, NativeModules, findNodeHandle } from 'react-native';
import React from 'react'
import PropTypes from 'prop-types';
import resolveAssetSource from 'resolveAssetSource';
class QBMarker extends React.Component {
  render() {
    let { image } = this.props;

    image = resolveAssetSource(image);
    image = image.uri ? image.uri : '';
    
    return (
      <RNMarker 
        ref={(ref) => this.ref = ref}
        {...this.props}
        image={image}
      />
    )
  }
}
QBMarker.propTypes = {
  ...View.propTypes,
  coordinate: PropTypes.shape({
    lat: PropTypes.number.isRequired,
    lng: PropTypes.number.isRequired,
  }).isRequired,
  draggable: PropTypes.bool,
  title: PropTypes.string,
  image: PropTypes.any
};
const RNMarker = requireNativeComponent('QBMapMarker', QBMarker, {
  // nativeOnly: {
  //   onMapLoaded: PropTypes.func
  // }
});

module.exports = QBMarker;