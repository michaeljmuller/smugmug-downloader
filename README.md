# smugmug-downloader
Downloads all the images an a SmugMug account for backup purposes

The following environment variables must be set for the application to run properly:
- API_KEY
- API_SECRET
- USER_KEY
- USER_SECRET
- TARGET_DIRECTORY

The API key and secret can be obtained by [applying as a developer](https://api.smugmug.com/api/developer/apply) on SmugMug's web site.

The user key and secret can be obtained from SmugMug's [privacy page](https://www.smugmug.com/app/account/settings/?#section=privacy) under "Authorized
Services".

The target directory should be set to the place on the local file system you want the photos written.

## Credit
I used [tommyblue/smugmug-backup](https://github.com/tommyblue/smugmug-backup) as a starting point for my development.
