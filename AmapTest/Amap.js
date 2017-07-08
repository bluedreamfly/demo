import { requireNativeComponent, View, NativeModules, findNodeHandle } from 'react-native';
import React from 'react'
import PropTypes from 'prop-types';

class Amap extends React.Component {
  render() {
    return (
      <RNAmap 
        ref={(ref) => this.ref = ref}
        {...this.props}
        onChange={this.props.didSelectAnnatation}
      />
    )
  }

  addPoint(obj) {
    NativeModules.Amap.addPoint(findNodeHandle(this.ref), obj);
  }

  getPath(start, end) {
    NativeModules.Amap.getRoutePath(start, end);
  }

}
Amap.propTypes = {
  ...View.propTypes,
  didSelectAnnatation: PropTypes.func
};
const RNAmap = requireNativeComponent('Amap', Amap, {
  nativeOnly: {
    onChange: true
  }
});

module.exports = Amap;