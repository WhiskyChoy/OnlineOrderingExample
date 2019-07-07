export let getDetailLocation = (location) => {
    let str = `${location.province}
                ${location.province ? ',' : ''}
                ${location.city}
                ${location.city ? ',' : ''}
                ${location.district}
                ${location.district ? ',' : ''}
                ${location.street}
                ${location.street ? ',' : ''}
                ${location.streetNumber}
                ${location.streetNumber ? ',' : ''}`;
    return str.substring(0, str.lastIndexOf(','));
};

export let getAbsoluteLocation = (location) => {
    return `(${location.longitude}, ${location.latitude})`
};

export let getDetailLocationNoSplit = (location) => {
    return getAbsoluteLocation(location) + ' ' + getDetailLocation(location).replace(/,/g, '').replace(/\r\n/g, '').replace(/\n/g, '').replace(/ /g, '');
};
