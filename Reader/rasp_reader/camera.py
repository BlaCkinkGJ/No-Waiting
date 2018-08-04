from SimpleCV import Camera
import zbar
from PIL import Image

class QRScan:
    def __init__(self):
        self.scanner = zbar.ImageScanner()
        self.camera  = Camera()
        self.prev    = None

        self.scanner.parse_config('enable')

    def image_from_camera(self):
        camera     = self.camera
        image      = camera.getImage()
        image_data = image.getPIL().convert('L')

        self.size     = image_data.size
        self.raw_data = image_data.tostring()

    def scanning(self):
        width, height = self.size
        raw           = self.raw_data
        image         = zbar.Image(width, height, 'Y800', raw)
        self.scanner.scan(image)
        for symbol in image:
            return symbol.data
        return None
