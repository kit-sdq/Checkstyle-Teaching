"""Provide easy access to ressources stored in the pythomat.zip file."""
import os
import zipfile

from .exc import ConfigurationError

pythomat_zip = None

def read(name):
    """Read the contents of a pythomat ressource."""
    global pythomat_zip

    if not pythomat_zip:
        pythomat_zip = zipfile.ZipFile("pythomat.zip", 'r', zipfile.ZIP_STORED)

    path = os.path.join("ressources", name)
    path = path.replace(os.sep, "/")
    try:
        result = pythomat_zip.read(path)
    except KeyError:
        raise ConfigurationError("No ressource named '" + name + "' available")

    return result

def list(group):
    """List all ressources in a ressource group."""
    global pythomat_zip

    if not pythomat_zip:
        pythomat_zip = zipfile.ZipFile("pythomat.zip", 'r')

    path = os.path.join("ressources", group)
    path = path.replace(os.sep, "/")
    return [os.path.relpath(member, "ressources") for member in pythomat_zip.namelist() if os.path.commonprefix([path, member]) == path]
