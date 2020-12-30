const client = filestack.init(fileStackAPI);

const options = {
    //onFileUploadFinished is called when the the user uploads a image in the
    // picker and they have successfully uploaded the image to filestack servers.
    onFileUploadFinished: callback =>{
        // I save the filestack image url to a const because I plan to use it in multiple places.
        const newImageUrl = callback.url;
        // this sets my hidden input to the value of my new image url.
        $('#image').val(newImageUrl);
        // this lets the user see a preview of the image that they uploaded.
        $('#imagePreview').attr('src', newImageUrl);
        // this lets the user change their profile image in users/edit
        $('#profileImg').val(newImageUrl);
        //Change the button to say "Change Picture" instead of "Add a Picture"
        $('#imageButton').text('Change Picture')
    }
}

$('#imageButton').click(function(e){
    // this is what prevents the button from submitting the form
    e.preventDefault();



    //we use this to tell filestack to open their file picker interface.
    // the picker method can take an argument of a options object
    // where you can specify what you want the picker to do
    client.picker(options).open();
})