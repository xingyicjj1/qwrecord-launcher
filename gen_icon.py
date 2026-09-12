#!/usr/bin/env python3
"""Generate launcher icons (mic style) without PIL deps if unavailable."""
import struct, zlib, os

def make_png(size, path):
    # Draw a red circle with white mic bar
    r = size // 2
    c = size / 2.0
    bg = (232, 56, 43)
    fg = (255, 255, 255)
    rows = []
    mic_w = int(size * 0.13)
    mic_h = int(size * 0.34)
    mic_top = int(size * 0.28)
    mic_bottom = mic_top + mic_h
    stand_w = int(size * 0.22)
    stand_h = int(size * 0.05)
    base_w = int(size * 0.30)
    for y in range(size):
        row = bytearray()
        for x in range(size):
            dx, dy = x - c + 0.5, y - c + 0.5
            inside = dx * dx + dy * dy <= r * r
            col = (0, 0, 0)
            a = 0
            if inside:
                a = 255
                col = bg
                # mic capsule
                if mic_top <= y <= mic_bottom and abs(dx) <= mic_w:
                    col = fg
                # capsule round caps approximation
                elif mic_top - mic_w <= y < mic_top and abs(dx) <= mic_w * 0.8:
                    col = fg
                elif mic_bottom < y <= mic_bottom + mic_w and abs(dx) <= mic_w * 0.8:
                    col = fg
                # stand
                elif mic_bottom + mic_w < y <= mic_bottom + mic_w + stand_h and abs(dx) <= stand_w:
                    col = fg
                # base
                elif mic_bottom + mic_w + stand_h < y <= mic_bottom + mic_w + stand_h + stand_h and abs(dx) <= base_w:
                    col = fg
            row += bytes((col[0], col[1], col[2], a))
        rows.append(bytes(row))

    raw = b''.join(b'\x00' + r_ for r_ in rows)

    def chunk(typ, data):
        c = struct.pack('>I', len(data)) + typ + data
        c += struct.pack('>I', zlib.crc32(typ + data) & 0xffffffff)
        return c

    png = b'\x89PNG\r\n\x1a\n'
    png += chunk(b'IHDR', struct.pack('>IIBBBBB', size, size, 8, 6, 0, 0, 0))
    png += chunk(b'IDAT', zlib.compress(raw, 9))
    png += chunk(b'IEND', b'')
    open(path, 'wb').write(png)
    print('wrote', path, len(png), 'bytes')

if __name__ == '__main__':
    d = '/root/qwrecord-apk/www'
    os.makedirs(d, exist_ok=True)
    for s in (192, 512):
        make_png(s, os.path.join(d, f'icon-{s}.png'))
